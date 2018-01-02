package org.springframework.samples.petclinic.admin.model;
//package org.springframework.samples.petclinic.admin.model;
//
//import java.io.Serializable;
//import java.util.List;
//
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//
//import org.apache.commons.lang.builder.ToStringBuilder;
//
//import com.fasterxml.jackson.annotation.JsonInclude;
//import com.fasterxml.jackson.annotation.JsonProperty;
//import com.fasterxml.jackson.annotation.JsonPropertyOrder;
//
//import de.zapJsonReport.Alerts;
//
//@Entity
//@JsonInclude(JsonInclude.Include.NON_NULL)
//@JsonPropertyOrder({
//"alerts"
//})
//public class ZapJsonReport implements Serializable {
//
//	
//private final static long serialVersionUID = -1521514328935189542L;
//
//@JsonProperty("alerts")
//private  List<Alerts>alerts = null;
//
//@Id
//@GeneratedValue(strategy = GenerationType.SEQUENCE)
//@JsonProperty("id")
//private int id;
//
///**
//* No args constructor for use in serialization
//* 
//*/
//public ZapJsonReport() {
//}
//
///**
//* 
//* @param alerts
//*/
//public ZapJsonReport( List<Alerts> alerts) {
//super();
//this.alerts = alerts;
//}
//
//@JsonProperty("alerts")
//public List<Alerts> getAlerts() {
//return alerts;
//}
//
//@JsonProperty("alerts")
//public void setAlerts( List<Alerts> alerts) {
//this.alerts = alerts;
//}
//
//@Override
//public String toString() {
//return ToStringBuilder.reflectionToString(this);
//}
//
//}
