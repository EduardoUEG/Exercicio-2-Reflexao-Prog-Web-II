package utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import anotacao.Campo;
import modelo.SuperTabela;

public class ReflexaoTabela {
    private static Field getPkField(SuperTabela tab) {
        validarParametroTab(tab);
        int numPks = 0;
        Field pkField = null;
        String pkNome = null;
        Class<?> cls = tab.getClass();
        Field[] atributos = cls.getDeclaredFields();

        for (Field attr : atributos) {
            if (attr.isAnnotationPresent(Campo.class)) {
                Campo cmp = attr.getAnnotation(Campo.class);
                if (cmp.isPk()) {
                    pkField = attr;
                    pkNome = cmp.colunaNome();
                    numPks++;
                }
            }
        }
        if (pkField == null || pkNome.isEmpty()) {
            throw new RuntimeException("Classe: " + cls.getName() + " não tem nenhum atributo anotado com @Campo(isPk=True)");
        } else if (numPks > 1) {
            throw new RuntimeException("Classe: " + cls.getName() + " tem mais de um atributo anotado com @Campo(isPk=True)");
        }
        return pkField;
    }

    public static String getPkName(SuperTabela tab) {
        Field pkField = getPkField(tab);
        Campo cmp = pkField.getAnnotation(Campo.class);
        return cmp.colunaNome();
    }

    public static Object getPkValue(SuperTabela tab) {
        Field pkField = getPkField(tab);
        String pkMethodName = "get" + getUCFirst(pkField.getName());
        return invokeGetMethod(tab, pkMethodName);
    }

    private static void validarParametroTab(SuperTabela tab) {
        if (tab == null) {
            throw new RuntimeException("Erro Metodo ReflexaoTabela.getPkNome, deve receber um objeto não nulo");
        }
    }

    private static Object invokeGetMethod(SuperTabela tab, String pkMethodName) {
        return invokeMethod(tab, pkMethodName, null);
    }

    private static Object invokeMethod(SuperTabela tab, String pkMethodName, Object value) {
        Class<?> cls = tab.getClass();
        Method pkMethod = null;
        try {
            if (value == null) {
                pkMethod = cls.getMethod(pkMethodName);
                return pkMethod.invoke(tab);
            } else {
                pkMethod = cls.getMethod(pkMethodName, value.getClass());
                return pkMethod.invoke(tab, value);
            }
        } catch (NoSuchMethodException | SecurityException e) {
            throw new RuntimeException("A classe: " + cls.getName() + " não possui o metodo: " + pkMethodName + "(" + (value!=null?value.getClass().getName():"") + ") com visibilidade pública, ou não existe!");
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new RuntimeException("Houve um erro desconhecido na execução do método:" + cls.getName() + "." + pkMethodName, e);
        }
    }

    public static String getUCFirst(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public static void setPkValue(SuperTabela<?> tab, Object value) {
        Field pkField = getPkField(tab);
        setValue(tab, pkField, value);
    }

    public static void setValue(SuperTabela<?> tab, Field field, Object value) {
        String pkMethodName = "set" + getUCFirst(field.getName());
        invokeMethod(tab, pkMethodName, value);
    }

    public static boolean validarCamposObrigatorios(SuperTabela tab) {
        validarParametroTab(tab);
        Class<?> cls = tab.getClass();
        Field[] atributos = cls.getDeclaredFields();

        for (Field attr : atributos) {
            if (attr.isAnnotationPresent(Campo.class)) {
                Campo cmp = attr.getAnnotation(Campo.class);
                if (cmp.isObrigatorio()) {
                    attr.setAccessible(true);
                    try {
                        Object valor = attr.get(tab);
                        if (valor == null || (valor instanceof String && ((String) valor).trim().isEmpty())) {
                            System.out.println("Campo obrigatório não preenchido: " + attr.getName()
                                    + " (coluna: " + cmp.colunaNome() + ")");
                            return false;
                        }
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Erro ao acessar campo obrigatório: " + attr.getName(), e);
                    }
                }
            }
        }
        return true;
    }
}
