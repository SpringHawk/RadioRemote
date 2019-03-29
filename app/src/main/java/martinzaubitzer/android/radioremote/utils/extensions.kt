package martinzaubitzer.android.radioremote.utils

import android.content.Context
import martinzaubitzer.android.radioremote.utils.data.NotesDatabaseOpenHelper

/**
 * Created by tsantana on 03/03/18.
 */

/**
 * Extension for the application context to add a reference to the database open helper
 */
val Context.database: NotesDatabaseOpenHelper
    get() = NotesDatabaseOpenHelper.getInstance(applicationContext)