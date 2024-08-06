package com.recursoconfiable.logisticabarrios

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Adaptador para el RecyclerView que muestra una lista de arribos.
class ArriboAdapter(
    private val arribos: List<Arribo>, // Lista de arribos a mostrar.
    private val onClick: (Arribo) -> Unit // Función de clic para manejar eventos de clic en los items.
) : RecyclerView.Adapter<ArriboAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textFolioLabel: TextView = itemView.findViewById(R.id.textFolioLabel)
        val textFolioValue: TextView = itemView.findViewById(R.id.textFolioValue)
        val textTarimasLabel: TextView = itemView.findViewById(R.id.textTarimasLabel)
        val textTarimasValue: TextView = itemView.findViewById(R.id.textTarimasValue)
        val textCajasLabel: TextView = itemView.findViewById(R.id.textCajasLabel)
        val textCajasValue: TextView = itemView.findViewById(R.id.textCajasValue)
        val textLineaTransporteLabel: TextView = itemView.findViewById(R.id.textLineaTransporteLabel)
        val textLineaTransporteValue: TextView = itemView.findViewById(R.id.textLineaTransporteValue)
        val textHorarioEntregaLabel: TextView = itemView.findViewById(R.id.textHorarioEntregaLabel)
        val textHorarioEntregaValue: TextView = itemView.findViewById(R.id.textHorarioEntregaValue)

        init {
            itemView.setOnClickListener {
                onClick(arribos[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_arribo, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val arribo = arribos[position]
        holder.textFolioLabel.text = "Folio:"
        holder.textFolioValue.text = arribo.folio
        holder.textTarimasLabel.text = "Tarimas:"
        holder.textTarimasValue.text = arribo.numero_de_tarimas.toString()
        holder.textCajasLabel.text = "Cajas:"
        holder.textCajasValue.text = arribo.numero_de_cajas.toString()
        holder.textLineaTransporteLabel.text = "Línea de transporte:"
        holder.textLineaTransporteValue.text = arribo.linea_de_transporte
        holder.textHorarioEntregaLabel.text = "Horario de entrega:"
        holder.textHorarioEntregaValue.text = arribo.horario_de_entrega

        val statusCircle: View = holder.itemView.findViewById(R.id.estatus)
        when (arribo.estatus) {
            "pendiente" -> {
                statusCircle.setBackgroundResource(R.drawable.status_circle_pending)
                val animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.blink)
                statusCircle.startAnimation(animation)
            }
            "completo" -> statusCircle.setBackgroundResource(R.drawable.status_circle_done)
            else -> statusCircle.setBackgroundResource(R.drawable.status_circle_alternate)
        }
    }

    override fun getItemCount(): Int = arribos.size
}
