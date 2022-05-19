package no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.service

import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.dokdistfordeling.Adresse
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.dokdistfordeling.Distribusjonstidspunkt
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.dokdistfordeling.Distribusjonstype

data class BrevDistribueringsInfo(
    val bestillendeFagsystem: String,
    val batchId: String? = null,
    val adresse: Adresse? = null,
    val dokumentProdApp: String,
    val distribusjonstype: Distribusjonstype,
    val distribusjonstidspunkt: Distribusjonstidspunkt? = null
)