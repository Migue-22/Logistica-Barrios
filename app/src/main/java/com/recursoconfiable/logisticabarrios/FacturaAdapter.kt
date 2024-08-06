package com.recursoconfiable.logisticabarrios

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.view.animation.AnimationUtils

class FacturaAdapter(
    private val facturas: List<Factura>,
    private val onClick: (Factura) -> Unit
) : RecyclerView.Adapter<FacturaAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textFolioLabel: TextView = itemView.findViewById(R.id.textFolioLabelFactura)
        val textFolioValue: TextView = itemView.findViewById(R.id.textFolioValueFactura)
        val textTarimasLabel: TextView = itemView.findViewById(R.id.textTarimasLabelFactura)
        val textTarimasValue: TextView = itemView.findViewById(R.id.textTarimasValueFactura)
        val textCajasLabel: TextView = itemView.findViewById(R.id.textCajasLabelFactura)
        val textCajasValue: TextView = itemView.findViewById(R.id.textCajasValueFactura)
        val textLineaTransporteLabel: TextView = itemView.findViewById(R.id.textLineaTransporteLabelFactura)
        val textLineaTransporteValue: TextView = itemView.findViewById(R.id.textLineaTransporteValueFactura)
        val textHorarioEntregaLabel: TextView = itemView.findViewById(R.id.textHorarioEntregaLabelFactura)
        val textHorarioEntregaValue: TextView = itemView.findViewById(R.id.textHorarioEntregaValueFactura)

        init {
            itemView.setOnClickListener {
                onClick(facturas[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_factura, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val factura = facturas[position]
        holder.textFolioLabel.text = "Folio:"
        holder.textFolioValue.text = factura.folio
        holder.textTarimasLabel.text = "Tarimas:"
        holder.textTarimasValue.text = factura.numero_de_tarimas.toString()
        holder.textCajasLabel.text = "Cajas:"
        holder.textCajasValue.text = factura.numero_de_cajas.toString()
        holder.textLineaTransporteLabel.text = "Línea de transporte:"
        holder.textLineaTransporteValue.text = factura.linea_de_transporte
        holder.textHorarioEntregaLabel.text = "Horario de entrega:"
        holder.textHorarioEntregaValue.text = factura.horario_de_entrega

        val statusCircle: View = holder.itemView.findViewById(R.id.estatusFactura)
        when (factura.estatus_factura) {
            "pendiente" -> {
                statusCircle.setBackgroundResource(R.drawable.status_circle_pending)
                val animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.blink)
                statusCircle.startAnimation(animation)
            }
            "completada" -> statusCircle.setBackgroundResource(R.drawable.status_circle_done)
            else -> statusCircle.setBackgroundResource(R.drawable.status_circle_alternate)
        }
    }

    override fun getItemCount(): Int = facturas.size
}
