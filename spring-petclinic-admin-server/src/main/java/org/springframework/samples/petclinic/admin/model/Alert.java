package org.springframework.samples.petclinic.admin.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;

	/**
	 * The persistent class for the alert database table.
	 * 
	 */
	@Entity
	@NamedQuery(name="Alert.findAll", query="SELECT a FROM Alert a")
	public class Alert implements Serializable {
		private static final long serialVersionUID = 1L;

		@Id
		@GeneratedValue(strategy=GenerationType.AUTO)
		private int alertid;

		@Lob
		private String alert;

		@Lob
		private String attack;

		private int cweid;

		@Lob
		private String description;

		@Lob
		private String evidence;

		private int historyid;

		@Lob
		private String otherinfo;

		@Lob
		private String param;

		private int pluginid;

		@Lob
		private String reference;

		private int reliability;

		private int risk;

		private int scanid;

		@Lob
		private String solution;

		private int sourcehistoryid;

		private int sourceid;

		@Lob
		private String uri;

		private int wascid;

		public Alert() {
		}

		public int getAlertid() {
			return this.alertid;
		}

		public void setAlertid(int alertid) {
			this.alertid = alertid;
		}

		public String getAlert() {
			return this.alert;
		}

		public void setAlert(String alert) {
			this.alert = alert;
		}

		public String getAttack() {
			return this.attack;
		}

		public void setAttack(String attack) {
			this.attack = attack;
		}

		public int getCweid() {
			return this.cweid;
		}

		public void setCweid(int cweid) {
			this.cweid = cweid;
		}

		public String getDescription() {
			return this.description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getEvidence() {
			return this.evidence;
		}

		public void setEvidence(String evidence) {
			this.evidence = evidence;
		}

		public int getHistoryid() {
			return this.historyid;
		}

		public void setHistoryid(int historyid) {
			this.historyid = historyid;
		}

		public String getOtherinfo() {
			return this.otherinfo;
		}

		public void setOtherinfo(String otherinfo) {
			this.otherinfo = otherinfo;
		}

		public String getParam() {
			return this.param;
		}

		public void setParam(String param) {
			this.param = param;
		}

		public int getPluginid() {
			return this.pluginid;
		}

		public void setPluginid(int pluginid) {
			this.pluginid = pluginid;
		}

		public String getReference() {
			return this.reference;
		}

		public void setReference(String reference) {
			this.reference = reference;
		}

		public int getReliability() {
			return this.reliability;
		}

		public void setReliability(int reliability) {
			this.reliability = reliability;
		}

		public int getRisk() {
			return this.risk;
		}

		public void setRisk(int risk) {
			this.risk = risk;
		}

		public int getScanid() {
			return this.scanid;
		}

		public void setScanid(int scanid) {
			this.scanid = scanid;
		}

		public String getSolution() {
			return this.solution;
		}

		public void setSolution(String solution) {
			this.solution = solution;
		}

		public int getSourcehistoryid() {
			return this.sourcehistoryid;
		}

		public void setSourcehistoryid(int sourcehistoryid) {
			this.sourcehistoryid = sourcehistoryid;
		}

		public int getSourceid() {
			return this.sourceid;
		}

		public void setSourceid(int sourceid) {
			this.sourceid = sourceid;
		}

		public String getUri() {
			return this.uri;
		}

		public void setUri(String uri) {
			this.uri = uri;
		}

		public int getWascid() {
			return this.wascid;
		}

		public void setWascid(int wascid) {
			this.wascid = wascid;
		}

	

}
