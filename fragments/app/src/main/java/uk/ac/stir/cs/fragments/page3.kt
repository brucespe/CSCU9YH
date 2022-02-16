package uk.ac.stir.cs.fragments

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import uk.ac.stir.cs.fragments.databinding.FragmentPage3Binding
import android.text.method.ScrollingMovementMethod
import android.widget.DatePicker
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import java.util.*


class page3 : Fragment(), DatePickerDialog.OnDateSetListener {

    //Binding, viewModel and dataPasser variables
    private var _binding : FragmentPage3Binding? = null
    private val binding get() = _binding!!
    lateinit var model: SharedViewModel
    lateinit var dataPasser: OnDataPass

    //Variables for the dates
    var day = 0
    var month = 0
    var year = 0

    //Saved values assigned when date is selected on pop up
    var savedDay = 0
    var savedMonth = 0
    var savedYear = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        _binding = FragmentPage3Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        //If there are lots of stored notes, adds scroll wheel to displayed output
        binding.txtNotes.movementMethod = ScrollingMovementMethod()

        //Confirm date cannot be clicked until date is selected
        binding.btnDeleteDate.alpha = .4f
        binding.btnDeleteDate.isClickable = false

        val notes = getNotes()
        //Remove brackets and colon, they are present when printing lists
        val notesString = notes.toString().replace("[", "").replace("]", "").replace(",","").trim()
        //Display the notes from the database
        binding.txtNotes.text=notesString

        binding.btnRefresh.setOnClickListener { //Refresh notes button
            //btnDelete should only be clickable when a filter is selected
            //User may click refresh while on a filter so need to ensure its set to not clickable
            binding.btnDeleteDate.alpha = .4f
            binding.btnDeleteDate.isClickable = false
            //Toast message to infrom users
            Toast.makeText(activity, "Refreshing Notes...", Toast.LENGTH_LONG).show()
            //Get notes and convert them into good printable format
            val notes = getNotes()
            var notesString = notes.toString()
            if (notesString == "[]"){  //If there are no notes to display
                binding.txtNotes.text="No Notes Found"
            }else{  //If database contains notes
                notesString = notesString.replace("[", "").replace("]", "").replace(",","").trim()
                binding.txtNotes.text=notesString
            }
        }
        binding.btnClearNotes.setOnClickListener { //Clear notes button
            binding.btnDeleteDate.alpha = .4f
            binding.btnDeleteDate.isClickable = false
            //Function to delete all notes in database
            clearNotes()
            //Display message, also toast message to inform user of deletion
            binding.txtNotes.text="All Notes Deleted"
            Toast.makeText(activity, "All notes have been deleted", Toast.LENGTH_SHORT).show()
        }
        binding.btnFilter.setOnClickListener {
            //get current date
            getDateCalendar()
            //Display calender to pick date, date already selected is current date
            DatePickerDialog(requireContext(), this, year, month, day).show()
        }
        binding.btnNewNote.setOnClickListener { //New note button
            binding.btnDeleteDate.alpha = .4f
            binding.btnDeleteDate.isClickable = false
            passData(0) //Back to date selector screen
        }
    }

    /**
     *Function to assign global date variables to current date
     */
    private fun getDateCalendar(){
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
    }

    /**
     * Function to destroy binding when activity ends
     */
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    /**
     * Function to attach dataPasser to this context
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataPasser = context as OnDataPass
    }

    /**
     * Function to delete a note by selected ID
     */
     private fun deleteNote(date_id: String) {
        dataPasser.deleteNote(date_id)
    }

    /**
     * Passes data (screen to change to) to the dataPasser, within override functions in main
     */
    private fun passData(data: Int){
        dataPasser.onDataPass(data)
    }

    /**
     * Function to get all the notes currently stored in the database
     * @return List<Note> - List of notes from the database
     */
    private fun getNotes():List<Note>{
        return dataPasser.getNotes()
    }

    /**
     * Function to get all the notes currently stored in the database
     * @return List<Note> - List of notes from the database
     */
    private fun containsPrimaryKey(date: String): Boolean {
        return dataPasser.containsPrimaryKey(date)
    }

    private fun loadAllByIds(date: String): List<Note> {
        return dataPasser.loadAllByIds(date)
    }

    /**
     * Function to clear all notes currently in the database
     */
    private fun clearNotes(){
        dataPasser.onDeletion()
    }

    /**
     * Function that runs when the date is selected
     *
     * @param p0 - The datepicker
     * @param year - Year selected
     * @param month - Year selected
     * @param dayOfMonth - Year selected
     */
    override fun onDateSet(p2: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        //Set the dates selected to the saved date variables
        savedDay = dayOfMonth
        savedMonth = month
        savedYear = year

        //Main code runs before onDateSet does, meaning will check for date 0-0-0 while date dialoguebox is open
        //Adding this function call means the filters will only be applied once the date has been set first
        displayFilters()
    }

    /**
     * Function for displaying notes under a specified date
     */
    private fun displayFilters(){
        //Formatted date that has been selected
        var dateFilter = "$savedDay-$savedMonth-$savedYear"

        //Checks if there is a note for this date
        if (containsPrimaryKey(dateFilter)){    //Note found
            //Toast message informing user of success
            Toast.makeText(activity, "Notes for $dateFilter have been found", Toast.LENGTH_SHORT).show()
            //Get note for this date
            val notesDate = loadAllByIds(dateFilter)
            //Remove brackets and colon, they are present when printing lists
            val notesString = notesDate.toString().replace("[", "").replace("]", "").replace(",","").trim()
            //Display the notes from the database
            binding.txtNotes.text=notesString
            //Makes delete button clickable again
            binding.btnDeleteDate.alpha = 1f
            binding.btnDeleteDate.isClickable = true
            binding.btnDeleteDate.setOnClickListener {
                //Set delete button to unable to edit again
                binding.btnDeleteDate.alpha = .4f
                binding.btnDeleteDate.isClickable = false
                deleteNote(dateFilter)
                //Confirmation message
                binding.txtNotes.text="No notes for $dateFilter have been found"
                Toast.makeText(activity, "Notes for $dateFilter have been deleted", Toast.LENGTH_SHORT).show()

            }

        }else if(!containsPrimaryKey(dateFilter)) { //No note found, error messaage
            binding.txtNotes.text="No Notes Found"
            Toast.makeText(activity, "No notes have been found for $dateFilter", Toast.LENGTH_SHORT).show()
        }
    }
}