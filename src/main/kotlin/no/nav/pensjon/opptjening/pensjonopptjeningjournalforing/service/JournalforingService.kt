package no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.service

import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.brevbaking.BrevbakerClient
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.brevbaking.model.BrevbakingRequest
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.dokdistfordeling.DokDistClient
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.dokdistfordeling.DokDistRequest
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.journalforing.JournalforingClient
import org.springframework.stereotype.Service

@Service
class JournalforingService(
    private val brevbakerClient: BrevbakerClient,
    private val journalforingClient: JournalforingClient,
    private val dokDistClient: DokDistClient
) {
    /**
     * fnr
     * sakId
     * brevkode
     * LetterRequest eller innhold i brev
     *
     */

    fun journalfor(journalforingInfo: JournalforingInfo, brevDistribueringsInfo: BrevDistribueringsInfo, request: BrevbakingRequest) {
        val brevBakerResponse = brevbakerClient.lagBrev(journalforingInfo.brevKode,request)
        val opprettJournalpostResponse = journalforingClient.opprettJournalpost(journalforingInfo,brevBakerResponse)
        val dokDistRequest = DokDistRequest(brevDistribueringsInfo, opprettJournalpostResponse.journalpostId)
        val distribuerBrevResponse = dokDistClient.distribuerBrev(dokDistRequest)
    }
}