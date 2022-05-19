package no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.service

import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.brevbaking.BrevbakerClient
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.brevbaking.model.BrevbakingRequest
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.dokdistfordeling.DokDistClient
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.dokdistfordeling.DokDistRequest
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.journalforing.JournalforingClient
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.journalforing.OpprettJournalpostRequest
import org.springframework.stereotype.Service

@Service
class JournalforingService(
    private val brevbakerClient: BrevbakerClient,
    private val journalforingClient: JournalforingClient,
    private val dokDistClient: DokDistClient
) {

    fun journalfor(journalforingInfo: JournalforingInfo, brevDistribueringsInfo: BrevDistribueringsInfo, brevbakingRequest: BrevbakingRequest) {
        val brevBakerResponse = brevbakerClient.lagBrev(journalforingInfo.brevKode,brevbakingRequest)

        val opprettJournalpostRequest = OpprettJournalpostRequest(journalforingInfo,brevBakerResponse)
        val opprettJournalpostResponse = journalforingClient.opprettJournalpost(opprettJournalpostRequest)

        val dokDistRequest = DokDistRequest(brevDistribueringsInfo, opprettJournalpostResponse.journalpostId)
        val distribuerBrevResponse = dokDistClient.distribuerBrev(dokDistRequest)
    }
}