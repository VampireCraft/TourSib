package dt.prod.patternvm.core.network

import dt.prod.patternvm.BaseApp

object TokenRepository {
    var accessToken: String = ""

    fun saveTokens(_accessToken: String) {
        setTokensToLocal(_accessToken)
        saveTokensToSharedPref(_accessToken)
    }

    private fun setTokensToLocal(_accessToken: String) {
        accessToken = _accessToken
    }

    private fun saveTokensToSharedPref(_accessToken: String) {
        BaseApp.sharedPreferences.edit().putString("accessToken", _accessToken).apply()
    }

    fun loadTokenFromShared(_accessToken: String) {
        setTokensToLocal(_accessToken)
    }

    fun deleteTokens() {
        accessToken = ""
        BaseApp.sharedPreferences.edit().putString("accessToken", "").apply()
    }
}