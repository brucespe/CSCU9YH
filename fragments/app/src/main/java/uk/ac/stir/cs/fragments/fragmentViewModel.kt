package uk.ac.stir.cs.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData

/**
 * ViewModel class used for storing and observing the date from page 1 fragment to page 2 using LiveData
 */
class SharedViewModel: ViewModel() {

    //LiveData varaible of the date, used to be observed by page 2 fragment and display the date
    private var _date = MutableLiveData("")
    val date: LiveData<String> = _date

    /**
     * Function for saving the data in a suitable format to the liveData variable
     * @param newDay - Day being added
     * @param newMonth - Month being added
     * @param newYear - Year being added
     */
    fun saveDate(newDay: Int, newMonth:Int, newYear:Int){
        _date.value = ("$newDay-$newMonth-$newYear")
    }
}
