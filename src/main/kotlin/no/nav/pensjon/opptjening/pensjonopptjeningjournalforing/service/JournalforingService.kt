package no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.service

import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.brevbaking.BrevbakerClient
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.dokdistfordeling.DokDistClient
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.journalforing.JournalforingClient
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.postadresse.PostadresseClient

class JournalforingService {

    fun journalfor(postadresseClient: PostadresseClient,
                   brevbakerClient: BrevbakerClient,
                   journalforingClient: JournalforingClient,
                   dokDistClient: DokDistClient) {

        //TODO slå opp postadresse for person

        //TODO bak brev

        //TODO opprett journalpost for bakt brev

        //TODO distribuer brev

    }
}