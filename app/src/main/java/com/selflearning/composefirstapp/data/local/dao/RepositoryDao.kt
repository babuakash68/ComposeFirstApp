package com.selflearning.composefirstapp.data.local.dao
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.selflearning.composefirstapp.data.local.entities.RepositoryEntity

@Dao
interface RepositoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRepositories(repositories: List<RepositoryEntity>)

    @Query("SELECT * FROM repositories")
    suspend fun getAllRepositories(): List<RepositoryEntity>

    @Query("DELETE FROM repositories")
    suspend fun clearRepositories()
}
