package cz.ash.mobilniapplikace.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesDao {
    @Query("SELECT * FROM favorite_coins ORDER BY name ASC")
    fun observeAll(): Flow<List<FavoriteCoinEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: FavoriteCoinEntity)

    @Query("DELETE FROM favorite_coins WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_coins WHERE id = :id)")
    fun observeIsFavorite(id: String): Flow<Boolean>

    @Query("DELETE FROM favorite_coins")
    suspend fun clearAll()
}

