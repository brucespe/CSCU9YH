package uk.ac.stir.cs.fragments

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Database used for storing notes
 */
@Database(entities = [Note::class], version = 1)
abstract class NoteRoomDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao //Database Access Object for database interactions
    companion object{
        var INSTANCE: NoteRoomDatabase?=null
        fun getInstance(context: Context):NoteRoomDatabase
        {
            if(INSTANCE==null){ //If an instance of this class doesn't already exist
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    NoteRoomDatabase::class.java, "note_database"   //Name of database
                ).allowMainThreadQueries().build()
            }
            return INSTANCE!!   //Return this instance
        }
    }
}
