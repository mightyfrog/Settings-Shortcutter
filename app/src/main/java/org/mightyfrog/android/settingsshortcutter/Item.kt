package org.mightyfrog.android.settingsshortcutter

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author Shigehiro Soejima
 */
class Item {
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("api")
    @Expose
    var api: Int? = null
    @SerializedName("action")
    @Expose
    var action: String? = null
    @SerializedName("constant")
    @Expose
    var constant: String? = null
}