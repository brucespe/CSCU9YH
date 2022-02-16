package uk.ac.stir.cs.fragments

/**
 * Inteface for passing data, these methods are overwritten in main
 */
interface OnDataPass {
        fun onDataPass(page: Int)

        fun newNote(date: String, note: String)

        fun onDeletion()

        fun getNotes():List<Note>

        fun deleteNote(date_id:String)

        fun containsPrimaryKey(date_id:String):Boolean

        fun updateNote(date:String, note:String)

        fun loadAllByIds(date:String): List<Note>

}