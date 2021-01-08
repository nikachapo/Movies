package com.example.movies.account

import androidx.test.core.app.ApplicationProvider
import com.example.movies.BaseCoroutinesTest
import com.example.movies.db.AccountRepository
import com.example.movies.db.AccountRepositoryImpl
import com.example.movies.fakes.FakeAccountDao
import com.example.movies.fakes.FakeLocalStorage
import com.example.movies.network.AccountRemoteService
import com.example.movies.utils.getOrAwaitValue
import com.example.movies.utils.util.KEY_ACCOUNT_ID
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class AccountManagerTest : BaseCoroutinesTest(){

    private lateinit var accountManager: AccountManager
    private lateinit var accountRepository: AccountRepository

    private lateinit var fakeLocalStorage: FakeLocalStorage
    private val fakeAccountDao = FakeAccountDao()

    private val account = Account(
        "111",
        "testName",
        "testUrl",
        "testEmail",
        "testNumber",
        "testFirstName",
        "testLastName"
    )

    @Mock
    private lateinit var mockAccountRemoteService: AccountRemoteService

    @Mock
    private lateinit var firebaseAuth: FirebaseAuth

    @Mock
    private lateinit var firebaseAuthUI: AuthUI

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        accountRepository = AccountRepositoryImpl(mockAccountRemoteService, fakeAccountDao)
        fakeLocalStorage = FakeLocalStorage()
        accountManager = AccountManager(
            accountRepository,
            fakeLocalStorage,
            firebaseAuth,
            firebaseAuthUI,
            Dispatchers.Unconfined
        )
    }

    @Test
    fun `loginAccount - save uid is not saved to local storage if user is not registered`() {
        mainCoroutineRule.runBlockingTest {
            `when`(firebaseAuth.uid).thenReturn(account.id)
            `when`(mockAccountRemoteService.fetchAccountData(account.id)).thenReturn(null)
            accountManager.logInAccount()
            assertThat(fakeLocalStorage.getObjectAtLocation(KEY_ACCOUNT_ID), nullValue())
        }
    }

    @Test
    fun `loginAccount - save uid is saved to local storage if user is registered`() {
        mainCoroutineRule.runBlockingTest {
            `when`(firebaseAuth.uid).thenReturn(account.id)
            `when`(mockAccountRemoteService.fetchAccountData(account.id))
                .thenReturn(account)
            accountManager.logInAccount()
            assertThat(fakeLocalStorage.getObjectAtLocation(KEY_ACCOUNT_ID), `is`(account.id))
        }
    }

    @Test
    fun `isAccountLoggedIn - is false when User is not authenticated with firebase`() {
        mainCoroutineRule.runBlockingTest {
            `when`(firebaseAuth.uid).thenReturn(null)
            assertThat(accountManager.isAccountLoggedIn(), not(true))
        }
    }

    @Test
    fun `isAccountLoggedIn - is true when User is authenticated and id is saved to prefs`() {
        mainCoroutineRule.runBlockingTest {
            `when`(firebaseAuth.uid).thenReturn(account.id)
            fakeAccountDao.insertAccount(account)
            fakeLocalStorage.setObjectAtLocation(KEY_ACCOUNT_ID, account.id)
            assertThat(accountManager.isAccountLoggedIn(), `is`(true))
        }
    }

    @Test
    fun `currentAccount - equals account from DAO`() {
        mainCoroutineRule.runBlockingTest {
            `when`(firebaseAuth.uid).thenReturn(account.id)
            `when`(mockAccountRemoteService.registerAccount(account)).thenReturn(true)
            accountManager.registerAccount(account)
            val accountLD = accountManager.currentAccount.getOrAwaitValue()
            assertThat(accountLD, `is`(account))
        }
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    fun `log out test`() {
        val mockTask: Task<Void> = mock(Task::class.java) as Task<Void>
        mainCoroutineRule.runBlockingTest {
            `when`(firebaseAuthUI.signOut(ApplicationProvider.getApplicationContext())).thenReturn(mockTask)
            mockTask.onSuccessTask {
                return@onSuccessTask mockTask
            }
            fakeLocalStorage.setObjectAtLocation(KEY_ACCOUNT_ID, account.id)
            accountManager.logOut(ApplicationProvider.getApplicationContext())
            assertThat(fakeLocalStorage.getObjectAtLocation(KEY_ACCOUNT_ID), `is`(""))
        }

    }
}