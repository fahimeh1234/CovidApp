package fahimeh.eltejaei.covidapp.model

import kotlinx.android.parcel.Parcelize


data class ResponseSummary(
    val Countries: List<Country>,
    val Date: String,
    val Global: Global,
    val ID: String,
    val Message: String
)