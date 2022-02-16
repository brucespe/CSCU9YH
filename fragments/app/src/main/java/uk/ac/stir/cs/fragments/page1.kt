package uk.ac.stir.cs.fragments

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import uk.ac.stir.cs.fragments.databinding.FragmentPage1Binding
import java.util.*


class Page1 : Fragment(R.layout.fragment_page1), DatePickerDialog.OnDateSetListener{

    //Binding for fragment, to allow access to elements
    private var _binding : FragmentPage1Binding? = null
    private val binding get() = _binding!!

    //Variables for the dates
    var day = 0
    var month = 0
    var year = 0

    //Saved values assigned when date is selected on pop up
    var savedDay = 0
    var savedMonth = 0
    var savedYear = 0

    //ViewModel to insert date
    lateinit var model: SharedViewModel
    //dataPasser to pass data to main
    lateinit var dataPasser: OnDataPass

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        //Inflate the layout for this fragment
        _binding = FragmentPage1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Instantiate the ViewModel
        model = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        //Confirm date cannot be clicked until date is selected
        binding.btnConfirm.alpha = .4f
        binding.btnConfirm.isClickable = false

        //Get calendar and allow user to select a date
        binding.btnDatePicker.setOnClickListener {
            //get current date
            getDateCalendar()
            //Display calender to pick date, date already selected is current date
            DatePickerDialog(requireContext(), this, year, month, day).show()
            //To handle confirm button
            pickDate()
        }
        //View notes button changes view to page 3 where the notes are displayed
        binding.btnViewNotes.setOnClickListener {
            passData(2)
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
     *Function to confirm date selection
     */
    private fun pickDate(){
            binding.btnConfirm.setOnClickListener {
                //Save date to ViewModel, to then be displayed on page 2
                model.saveDate(savedDay, savedMonth, savedYear)

                //Set confirm button to unable to edit again
                binding.btnConfirm.alpha = .4f
                binding.btnConfirm.isClickable = false

                //Clear the save date
                savedDay=0
                savedMonth=0
                savedYear=0

                //Back to default values and move to page 2
                binding.txtDate.text="DD-MM-YY"
                binding.txtReturn.text="No Date Selected"
                passData(1)
            }
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
     * Passes data (screen to change to) to the dataPasser, within override functions in main
     */
    private fun passData(data: Int){
        dataPasser.onDataPass(data)
    }

    /**
     * Function that runs when the date is selected
     *
     * @param p0 - The datepicker
     * @param year - Year selected
     * @param month - Year selected
     * @param dayOfMonth - Year selected
     */
    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        //Set the dates selected to the saved date variables
        savedDay = dayOfMonth
        savedMonth = month
        savedYear = year

        //Makes confirm button unclickable again
        binding.btnConfirm.alpha = 1f;
        binding.btnConfirm.isClickable = true;

        //Display the date that has been entered
        binding.txtReturn.text="Date Selected"
        binding.txtDate.text = "$savedDay-$savedMonth-$savedYear"
    }
}
