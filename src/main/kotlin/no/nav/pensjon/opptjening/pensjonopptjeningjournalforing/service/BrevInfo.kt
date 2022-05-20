package no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.service

import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.brevbaking.model.Felles

data class BrevInfo(val template: String, val letterData: Any, val felles: Felles)