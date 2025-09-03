package modelo;

import utils.ReflexaoTabela;
import anotacao.TableName;

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
	public String getTableName() {
        if (this.getClass().isAnnotationPresent(TableName.class)) {
            TableName nt = this.getClass().getAnnotation(TableName.class);
            return nt.value();
        }
        return this.getClass().getSimpleName();
    }

	public Boolean isCamposObrigatoriosPreenchidos(){
    		return ReflexaoTabela.validarCamposObrigatorios(this);
	}
}
