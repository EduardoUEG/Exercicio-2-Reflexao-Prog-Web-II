package modelo;

import utils.ReflexaoTabela;

public abstract class SuperTabela<TypePK> {
	public String getPkName() {
		return ReflexaoTabela.getPkName(this);
	}
	@SuppressWarnings("unchecked")
	public TypePK getPk() {
		return (TypePK) ReflexaoTabela.getPkValue(this);
	}
	public void setPk(TypePK value) {
		ReflexaoTabela.setPkValue(this,value);
	}
	public String getTableName(){
		return this.getClass().getSimpleName().toLowerCase();
	}

	public Boolean isCamposObrigatoriosPreenchidos(){
		// 1. utilize a classe de reflexão para determinar
		// se os compos obrigatórios estão preenchidos
		// 2. Criar a anotação Tabela(name="dssss") para mudar
		// o nome da tabela (Ajustar o metodo getTableName pra utilizar a anotação)
		return true;
	}
}
