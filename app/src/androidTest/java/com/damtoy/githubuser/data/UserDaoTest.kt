package com.damtoy.githubuser.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.damtoy.githubuser.data.local.AppDatabase
import com.damtoy.githubuser.data.local.UserDao
import com.damtoy.githubuser.data.local.UserEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: UserDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.userDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun searchByLogin_isCaseInsensitiveAndPartial() = runBlocking {
        dao.upsertBasic(listOf(UserEntity(1, "AdamN", "a"), UserEntity(2, "budi", "b")))

        val result = dao.searchByLogin("adam")

        assertEquals(listOf("AdamN"), result.map { it.login })
    }

    @Test
    fun upsertBasic_keepsCachedDetailFields() = runBlocking {
        dao.upsert(
            UserEntity(
                id = 1, login = "adam", avatarUrl = "old", name = "Adam", bio = "Dev",
                publicRepos = 3, followers = 1, following = 2,
                htmlUrl = "https://github.com/adam", isDetailCached = true
            )
        )

        dao.upsertBasic(listOf(UserEntity(1, "adam", "new")))

        val row = dao.getByLogin("ADAM")
        assertEquals("new", row?.avatarUrl)
        assertEquals("Adam", row?.name)
        assertTrue(row!!.isDetailCached)
    }

    @Test
    fun getByLogin_returnsNullWhenMissing() = runBlocking {
        assertNull(dao.getByLogin("ghost"))
    }
}
