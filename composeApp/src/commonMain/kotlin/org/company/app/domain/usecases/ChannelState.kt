package org.company.app.domain.usecases

import org.company.app.data.model.channel.Channel

sealed class ChannelState {
    object LOADING : ChannelState()
    data class SUCCESS(val channel: Channel) : ChannelState()
    data class ERROR(val error: String) : ChannelState()
}