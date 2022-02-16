package uk.ac.stir.cs.fragments

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * An entity class for the note, used to instantiate a note and then store it
 * @param date - The date for the note, used as a primary key
 * @param noteBody - The note body stored
 */
@Entity
data class Note(
    @PrimaryKey val date: String,
    @ColumnInfo(name = "note_body") val noteBody: String?
)

/**
 * toString method used for when printing the dates and notes
 * @return - String suitable for printing
 */
{
    override fun toString(): String = "$date - $noteBody\n\n"

}
