package fehidro.api.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

@Table(name = "tb_subcriterioavaliacao")
@Entity
public class SubcriterioAvaliacao extends AbstractEntity {
	
	@Column(name="nm_titulo")
	private String titulo;
	
	@Column(name="nr_subcriterio")
	private int numero;
	
	@Column(name="ic_letra")
	private char letra;
	
	@OneToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REMOVE}, orphanRemoval = true)
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@JoinColumn(name = "subcriterio_id")
	private List<Pontuacao> pontuacoes;
	
	@ManyToMany
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JoinTable(name = "tb_subcriterio_tb_tipoproposta", 
	 	joinColumns = {@JoinColumn(name = "subcriterio_id", referencedColumnName = "id")}, 
	 	inverseJoinColumns = {@JoinColumn(name = "tipoproposta_id", referencedColumnName = "id") })
	private List<TipoProposta> tiposProposta;
	 
	@ManyToOne
	@JsonIgnore
	private CriterioAvaliacao criterio;

	@OneToOne
	@JoinColumn(name = "perfilacesso_id")
	private PerfilAcesso perfilAcesso;
	
	public SubcriterioAvaliacao() {
		super();
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public List<Pontuacao> getPontuacoes() {
		return pontuacoes;
	}

	public void setPontuacoes(List<Pontuacao> pontuacoes) {
		this.pontuacoes = pontuacoes;
	}
	
	public void addPontuacoes(Pontuacao pontuacao) {
		if (this.pontuacoes.contains(pontuacao))
			return;
		
		this.pontuacoes.add(pontuacao);
		pontuacao.setSubcriterio(this);
	}
	
	public void removePontuacoes(Pontuacao pontuacao) {
		if (!this.pontuacoes.contains(pontuacao))
			return;
		
		this.pontuacoes.remove(pontuacao);
		pontuacao.setSubcriterio(null);
	}
		
	public int getNumero() {
		return numero;
	}
	
	public void setNumero(int numero) {
		this.numero = numero;
	}

	public List<TipoProposta> getTiposProposta() {
		return tiposProposta;
	}

	public void setTiposProposta(List<TipoProposta> tiposProposta) {
		this.tiposProposta = tiposProposta;
	}
	
	public void addTiposProposta(TipoProposta tipoProposta) {
		if(this.tiposProposta.contains(tipoProposta)) 
			return;
		
		this.tiposProposta.add(tipoProposta);
		tipoProposta.addSubcriterios(this);
	}
	
	public void removeTiposProposta(TipoProposta tipoProposta) {
		if(!this.tiposProposta.contains(tipoProposta)) 
			return;
		
		this.tiposProposta.remove(tipoProposta);
		tipoProposta.removeSubcriterios(this);
	}
	
	public char getLetra() {
		return letra;
	}

	public void setLetra(char letra) {
		this.letra = letra;
	}	
	
	public CriterioAvaliacao getCriterio() {
		return criterio;
	}

	public void setCriterio(CriterioAvaliacao criterio) {
		boolean mesmoCriterio = this.criterio == null ? criterio == null : this.criterio.equals(criterio);
		
		if (mesmoCriterio)
			return;
		
		CriterioAvaliacao antigo = this.criterio;		
		this.criterio = criterio;
		
		if (antigo != null) 
			antigo.removeSubcriterios(this);
		
		if (criterio != null)
			criterio.addSubcriterios(this);
	}

	public PerfilAcesso getPerfilAcesso() {
		return perfilAcesso;
	}

	public void setPerfilAcesso(PerfilAcesso perfilAcesso) {
		this.perfilAcesso = perfilAcesso;
	}
}
