package no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.service

import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.brevbaking.BrevbakerClient
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.dokdistfordeling.DokDistClient
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.journalforing.JournalforingClient
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.postadresse.PostadresseClient
import org.springframework.stereotype.Service

@Service
class JournalforingService(
    postadresseClient: PostadresseClient,
    brevbakerClient: BrevbakerClient,
    journalforingClient: JournalforingClient,
    dokDistClient: DokDistClient
) {
    /**
     * fnr
     * sakId
     * brevkode
     * LetterRequest eller innhold i brev
     *
     */

    fun journalfor() {

        //TODO slå opp postadresse for person

        //TODO bak brev

        //TODO opprett journalpost for bakt brev

        //TODO distribuer brev

    }
}