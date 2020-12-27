package com.example.movies.account

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.movies.MainCoroutineRule
import com.example.movies.db.AccountRepository
import com.example.movies.db.AccountRepositoryImpl
import com.example.movies.fakes.FakeAccountDao
import com.example.movies.fakes.FakeLocalStorage
import com.example.movies.network.AccountRemoteService
import com.example.movies.utils.getOrAwaitValue
import com.example.movies.utils.util.KEY_ACCOUNT_ID
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AccountManagerTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

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

    private lateinit var firebaseAuthUI: AuthUI

    @Before
    fun setUp() {
        accountRepository = AccountRepositoryImpl(mockAccountRemoteService, fakeAccountDao)
        fakeLocalStorage = FakeLocalStorage()
        firebaseAuthUI = AuthUI.getInstance()
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
            Mockito.`when`(firebaseAuth.uid).thenReturn(account.id)
            Mockito.`when`(mockAccountRemoteService.fetchAccountData(account.id)).thenReturn(null)
            accountManager.logInAccount()
            assertThat(fakeLocalStorage.getObjectAtLocation(KEY_ACCOUNT_ID), nullValue())
        }
    }

    @Test
    fun `loginAccount - save uid is saved to local storage if user is registered`() {
        mainCoroutineRule.runBlockingTest {
            Mockito.`when`(firebaseAuth.uid).thenReturn(account.id)
            Mockito.`when`(mockAccountRemoteService.fetchAccountData(account.id))
                .thenReturn(account)
            accountManager.logInAccount()
            assertThat(fakeLocalStorage.getObjectAtLocation(KEY_ACCOUNT_ID), `is`(account.id))
        }
    }

    @Test
    fun `isAccountLoggedIn - is false when User is not authenticated with firebase`() {
        mainCoroutineRule.runBlockingTest {
            Mockito.`when`(firebaseAuth.uid).thenReturn(null)
            assertThat(accountManager.isAccountLoggedIn(), not(true))
        }
    }

    @Test
    fun `isAccountLoggedIn - is true when User is authenticated and id is saved to prefs`() {
        mainCoroutineRule.runBlockingTest {
            Mockito.`when`(firebaseAuth.uid).thenReturn(account.id)
            fakeAccountDao.insertAccount(account)
            fakeLocalStorage.setObjectAtLocation(KEY_ACCOUNT_ID, account.id)
            assertThat(accountManager.isAccountLoggedIn(), `is`(true))
        }
    }

    @Test
    fun `currentAccount - equals account from DAO`() {
        mainCoroutineRule.runBlockingTest {
            Mockito.`when`(firebaseAuth.uid).thenReturn(account.id)
            Mockito.`when`(mockAccountRemoteService.registerAccount(account)).thenReturn(true)
            accountManager.registerAccount(account)
            val accountLD = accountManager.currentAccount.getOrAwaitValue()
            assertThat(accountLD, `is`(account))
        }
    }

    @Test
    fun `log out test`() {

    }
}