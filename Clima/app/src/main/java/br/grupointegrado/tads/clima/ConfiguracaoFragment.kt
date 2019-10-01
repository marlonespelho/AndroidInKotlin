package br.grupointegrado.tads.clima

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.preference.EditTextPreference
import android.support.v7.preference.ListPreference
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat

class ConfiguracaoFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.pref_clima)
        for (i in 0 until preferenceScreen.preferenceCount){
            val pref = preferenceScreen.getPreference(i)
            atualizarPreferenceSummary(pref)
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        val pref = findPreference(key)
        if (pref != null){
            atualizarPreferenceSummary(pref)
        }
    }

    fun atualizarPreferenceSummary(preference:Preference){
        if (preference is ListPreference){
            val metricaSelecionada = preferenceScreen.sharedPreferences.getString(preference.key,"")
            val itemSelecionado = preference.findIndexOfValue(metricaSelecionada)
            if (itemSelecionado >= 0){
                preference.summary = metricaSelecionada
            }
        }else if (preference is EditTextPreference){
            val cidadeSelecionada = preference.sharedPreferences.getString(preference.key,"")
            preference.summary = cidadeSelecionada
        }
    }

}