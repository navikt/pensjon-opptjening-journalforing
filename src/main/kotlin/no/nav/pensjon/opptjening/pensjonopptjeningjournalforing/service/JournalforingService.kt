package no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.service

import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.brevbaking.BrevbakerClient
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.brevbaking.model.BrevKode
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.brevbaking.model.LetterRequest
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.brevbaking.model.LetterResponse
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.dokdistfordeling.DokDistClient
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.dokdistfordeling.DokDistRequest
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.journalforing.JournalforingClient
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.journalforing.OpprettJournalpostResponse
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.journalforing.Sak
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.postadresse.PostadresseClient
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

    fun journalfor(journalpostInfo: JournalpostInfo, distribueringsInfo: DistribueringsInfo, request: LetterRequest) {
        val brevBakerResponse = brevbakerClient.lagBrev(journalpostInfo.brevKode,request)
        val opprettJournalpostResponse = journalforingClient.opprettJournalpost(journalpostInfo,brevBakerResponse)
        val dokDistRequest = DokDistRequest(distribueringsInfo, opprettJournalpostResponse.journalpostId)
        val distribuerBrevResponse = dokDistClient.distribuerBrev(dokDistRequest)
    }
}