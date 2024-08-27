package com.ecommerce.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
@Entity
public class Orden {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String numero;
	private Date fechacreacion;
	private Date fecharecibida;
	private double total;
	@ManyToOne
	private Usuario usuario;
	
	@OneToOne(mappedBy = "orden")
	private DetalleOrden detalleorden;
	
	
	public Orden() {
		super();
	}




	public Orden(int id, String numero, Date fechacreacion, Date fecharecibida, double total) {
		super();
		this.id = id;
		this.numero = numero;
		this.fechacreacion = fechacreacion;
		this.fecharecibida = fecharecibida;
		this.total = total;
	}




	public int getId() {
		return id;
	}




	public void setId(int id) {
		this.id = id;
	}




	public String getNumero() {
		return numero;
	}




	public void setNumero(String numero) {
		this.numero = numero;
	}




	public Date getFechacreacion() {
		return fechacreacion;
	}




	public void setFechacreacion(Date fechacreacion) {
		this.fechacreacion = fechacreacion;
	}




	public Date getFecharecibida() {
		return fecharecibida;
	}




	public void setFecharecibida(Date fecharecibida) {
		this.fecharecibida = fecharecibida;
	}




	public double getTotal() {
		return total;
	}




	public void setTotal(double total) {
		this.total = total;
	}




	public Usuario getUsuario() {
		return usuario;
	}




	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}




	public DetalleOrden getDetalleorden() {
		return detalleorden;
	}




	public void setDetalleorden(DetalleOrden detalleorden) {
		this.detalleorden = detalleorden;
	}




	@Override
	public String toString() {
		return "Orden [id=" + id + ", numero=" + numero + ", fechacreacion=" + fechacreacion + ", fecharecibida="
				+ fecharecibida + ", total=" + total + "]";
	}
	
	
}
