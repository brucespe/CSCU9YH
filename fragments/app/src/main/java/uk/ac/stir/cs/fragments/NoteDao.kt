package uk.ac.stir.cs.fragments

import androidx.room.*

/**
 * Database Access Object for database interactions
 */
@Dao
interface NoteDao{

    //Query to get list of all notes
    @Query("SELECT * FROM note")
    fun getAll(): List<Note>

    //For inserting a new note
    @Insert
    fun insert(note:Note)

    //Getting note from a selected date
    @Query("SELECT * FROM note WHERE date IN (:date_id)")
    fun loadAllByIds(date_id: String): List<Note>

    //Used for finding note in database by the note body. Not useful might remove?
    @Query("SELECT * FROM note WHERE note_body LIKE :noteBody LIMIT 1")
    fun findByDate(noteBody:String): List<Note>

    //Delete note from database
    @Query("DELETE FROM note WHERE date = :date_id")
    fun deleteNote(date_id: String);

    //Query to find a note under a specified date
    @Query("SELECT count(*)!=0 FROM note WHERE date IN (:date_id)")
    fun containsPrimaryKey(date_id: String): Boolean

    @Query("UPDATE note SET note_Body=:newNote WHERE date=:dateID")
    fun updateNote(dateID: String, newNote: String)

    //Delete all values stored in database
    @Query("DELETE FROM note")
    fun nukeTable()
}