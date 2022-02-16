package uk.ac.stir.cs.fragments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import java.util.*

class MainActivity : AppCompatActivity(), OnDataPass{
    private lateinit var viewPager : ViewPager2  //ViewPager for changing views

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Toolbar at top of page
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        //Setting the number of tabs and viewpager adapter
        viewPager = findViewById<ViewPager2>(R.id.pager)
        //Stop user from swiping to other fragments
        viewPager.isUserInputEnabled = false
        val adapter = MyPageAdapter(this,3)
        viewPager.adapter = adapter

        //Creating an example note
        val d1="26-11-2021"
        val n1="This note is an example previously stored"
        val n2 = Note(d1, n1)

        //Clearing database then adding the example note
        NoteRoomDatabase.getInstance(this@MainActivity).noteDao().nukeTable()       //Wipe the database on start up, used for debugging
        NoteRoomDatabase.getInstance(this@MainActivity).noteDao().insert(n2)        //Example note added
    }

    /**
     * Function used to change the fragment currently being viewed, to move to next fragment
     * @param page - The page number to be switched too
     */
    override fun onDataPass(page: Int) {
        viewPager.currentItem = page
    }

    /**
     * Function for adding a new note to the database
     * @param date - The date selected to add note to
     * @param note - The note body being added
     */
    override fun newNote(date: String, note: String) {
        val newN = Note(date, note) //New note object
        //Check if a note for date selected already exists
        if(!containsPrimaryKey(date)){
            //If note doesn't exist, create new note and insert it to database, then display toast message
            NoteRoomDatabase.getInstance(this@MainActivity).noteDao().insert(newN)
            Toast.makeText(this@MainActivity, "New note for $date added, please refresh note list", Toast.LENGTH_LONG).show()
        }
        else {
            //If note for this date already exists, then add to existing note
            val noteFound = NoteRoomDatabase.getInstance(this@MainActivity).noteDao().loadAllByIds(date)
            //Remove the date from the not found, or else note toString would contain 2 dates
            val notesString = noteFound.toString().drop(date.length+3).replace("[", "").replace("]", "").replace(",","").trim()
            val updatedNote = ("$notesString. $note")
            NoteRoomDatabase.getInstance(this@MainActivity).noteDao().updateNote(date, updatedNote)
            Toast.makeText(this@MainActivity, "Note for $date already exists, adding to your note ...", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Function to delete all entries in database, just helpful for debugging
     */
    override fun onDeletion() {
        NoteRoomDatabase.getInstance(this@MainActivity).noteDao().nukeTable()
    }

    /**
     * Function to get all entries in the database
     * @return List<Note> - List of notes stored
     */
    override fun getNotes(): List<Note> {
        return NoteRoomDatabase.getInstance(this@MainActivity).noteDao().getAll()
    }

    override fun deleteNote(date_id: String) {
        NoteRoomDatabase.getInstance(this@MainActivity).noteDao().deleteNote(date_id)
    }

    /**
     * Function to get all entries in the database
     * @return List<Note> - List of notes stored
     */
    override fun updateNote(date:String, note:String) {
        return NoteRoomDatabase.getInstance(this@MainActivity).noteDao().updateNote(date, note)
    }

    /**
     * Function to delete a note by selected ID
     */
    override fun loadAllByIds(date: String): List<Note> {
        return NoteRoomDatabase.getInstance(this@MainActivity).noteDao().loadAllByIds(date)
    }

    /**
     * Function to check if an entry for selected date already exists in database
     * @return pKey - True if key is found, false if no key found
     */
    override fun containsPrimaryKey(date_id:String): Boolean {
        return NoteRoomDatabase.getInstance(this@MainActivity).noteDao().containsPrimaryKey(date_id)
    }


}