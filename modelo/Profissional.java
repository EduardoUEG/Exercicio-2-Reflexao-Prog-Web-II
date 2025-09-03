package modelo;

import anotacao.Campo;
import utils.ReflexaoTabela;
import anotacao.TableName;

@TableName("tb_profissionais")
public class Profissional extends SuperTabela<Long>{
	@Campo(colunaNome="cpf",isPk=true, isObrigatorio=true)
	private Long cpf;
	@Campo(colunaNome="nome_profissional",isObrigatorio=true)
	private String nome;

	public Long getCpf() {
		return cpf;
	}

	public void setCpf(Long cpf) {
		this.cpf = cpf;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
}

