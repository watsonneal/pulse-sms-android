/*
 * Copyright (C) 2017 Luke Klinker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package xyz.klinker.messenger.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import xyz.klinker.messenger.R
import xyz.klinker.messenger.adapter.view_holder.ScheduledMessageViewHolder
import xyz.klinker.messenger.shared.data.model.ScheduledMessage
import xyz.klinker.messenger.shared.util.listener.ScheduledMessageClickListener
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Adapter for displaying scheduled messages in a recyclerview.
 */
class ScheduledMessagesAdapter(private val scheduledMessages: List<ScheduledMessage>, private val listener: ScheduledMessageClickListener?)
    : RecyclerView.Adapter<ScheduledMessageViewHolder>() {

    private val formatter: DateFormat = SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduledMessageViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_scheduled_message, parent, false)
        return ScheduledMessageViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ScheduledMessageViewHolder, position: Int) {
        val message = getItem(position)

        // phone number vs a known contact
        if (message.title == null || message.title!!.isEmpty()) {
            holder.titleDate.text = message.to + " - " + formatter.format(Date(message.timestamp))
        } else {
            holder.titleDate.text = message.title + " - " + formatter.format(Date(message.timestamp))
        }

        if (message.repeat != ScheduledMessage.REPEAT_NEVER) {
            val text = holder.titleDate.text.toString()
            holder.titleDate.text = "$text (" + when (message.repeat) {
                ScheduledMessage.REPEAT_DAILY -> holder.titleDate.context.getString(R.string.scheduled_repeat_daily)
                ScheduledMessage.REPEAT_WEEKLY -> holder.titleDate.context.getString(R.string.scheduled_repeat_weekly)
                ScheduledMessage.REPEAT_MONTHLY -> holder.titleDate.context.getString(R.string.scheduled_repeat_monthly)
                else -> holder.titleDate.context.getString(R.string.scheduled_repeat_yearly)
            } + ")"
        }

        holder.message.text = message.data
        holder.message.setOnClickListener { listener?.onClick(message) }
        holder.messageHolder.setOnClickListener { listener?.onClick(message) }
        holder.itemView.setOnClickListener { listener?.onClick(message) }

        holder.message.setOnLongClickListener {
            listener?.onClick(message)
            true
        }
        holder.messageHolder.setOnLongClickListener {
            listener?.onClick(message)
            true
        }
        holder.itemView.setOnLongClickListener {
            listener?.onClick(message)
            true
        }
    }

    override fun getItemCount() = scheduledMessages.size
    private fun getItem(position: Int) = scheduledMessages[position]
}
