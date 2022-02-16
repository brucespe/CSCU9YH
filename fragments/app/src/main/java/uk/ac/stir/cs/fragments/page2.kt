package uk.ac.stir.cs.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import uk.ac.stir.cs.fragments.databinding.FragmentPage2Binding


class page2 : Fragment() {

    //Binding, viewModel and dataPasser variables
    private var _binding : FragmentPage2Binding? = null
    private val binding get() = _binding!!
    lateinit var model: SharedViewModel
    lateinit var dataPasser: OnDataPass

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        //Inflate the layout for this fragment
        _binding = FragmentPage2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        //Observe the LiveData variable date from the viewModel
        model.date.observe(viewLifecycleOwner, Observer {
            //Display observed date
            binding.txtDate.text= it.toString()
        })

        binding.btnAddNote.setOnClickListener {//Add note button
            //Check if any text has been input to the textInput
            if (binding.txtNote.text.isEmpty()) { //If no input
                //Toast popup message to advise users
                Toast.makeText(activity, "Your note cannot be empty!", Toast.LENGTH_LONG).show()

            }else{ //If there is an input
                //Pass date and note input to newNote class
                newNote(binding.txtDate.text.toString(), binding.txtNote.text.toString())
                binding.txtNote.setText("")
                //Change view to page 3 where notes are displayed
                passData(2)
            }
        }
        binding.btnBack.setOnClickListener {//Back button
            //Clear the text inputted and change view to page 1
            binding.txtNote.setText("")
            passData(0)
        }
        binding.btnClearNote.setOnClickListener {//Clear button
            //Clear the text inputted and display toast message informing
            binding.txtNote.setText("")
            Toast.makeText(activity, "Note Cleared", Toast.LENGTH_LONG).show()
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
     * Function to add the note, passes it to main activity and is then added to database
     * @param date - Date selected
     * @param note - Note entered
     */
    private fun newNote(date:String, note:String){
        dataPasser.newNote(date, note)
    }

}