package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.StudentResults
import kotlinx.coroutines.flow.Flow

/**
 * Room Data Access Object for 'StudentResults' storing subject-wise examination
 * results for classes 8th through 12th.
 */
@Dao
interface StudentResultsDao {

    @Query("SELECT * FROM student_subject_results ORDER BY classLevel ASC, rollNumber ASC")
    fun getAllStudentResults(): Flow<List<StudentResults>>

    @Query("SELECT * FROM student_subject_results WHERE isPublished = 1 ORDER BY classLevel ASC, rollNumber ASC")
    fun getPublishedStudentResults(): Flow<List<StudentResults>>

    @Query("SELECT * FROM student_subject_results WHERE classLevel = :classLevel ORDER BY rollNumber ASC")
    fun getStudentResultsByClass(classLevel: String): Flow<List<StudentResults>>

    @Query("SELECT * FROM student_subject_results WHERE classLevel IN ('8th', '9th', '10th', '11th', '12th') AND isPublished = 1 ORDER BY classLevel ASC, rollNumber ASC")
    fun getClasses8To12Results(): Flow<List<StudentResults>>

    @Query("SELECT * FROM student_subject_results WHERE rollNumber = :rollNumber LIMIT 1")
    fun getStudentResultByRoll(rollNumber: String): Flow<StudentResults?>

    @Query("SELECT * FROM student_subject_results WHERE id = :id LIMIT 1")
    suspend fun getStudentResultById(id: String): StudentResults?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudentResult(result: StudentResults)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllStudentResults(results: List<StudentResults>)

    @Update
    suspend fun updateStudentResult(result: StudentResults)

    @Query("UPDATE student_subject_results SET isPublished = :isPublished WHERE id = :id")
    suspend fun updatePublishStatus(id: String, isPublished: Boolean)

    @Delete
    suspend fun deleteStudentResult(result: StudentResults)

    @Query("DELETE FROM student_subject_results WHERE id = :id")
    suspend fun deleteStudentResultById(id: String)
}
