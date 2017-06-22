package org.mightyfrog.android.settingsshortcutter

import com.google.gson.annotations.SerializedName

/**
 * @author Shigehiro Soejima
 */
data class Item(@SerializedName("name")
                var name: String?,
                @SerializedName("api")
                var api: Int?,
                @SerializedName("action")
                var action: String?,
                @SerializedName("constant")
                var constant: String?)