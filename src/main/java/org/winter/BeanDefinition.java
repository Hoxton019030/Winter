package org.winter;

/**
 * @author Hoxton
 * @version 0.1.0
 * @since 0.1.0
 **/
public class BeanDefinition {

    /**
     * 當前Bean類型
     */
    private Class clazz;

    /**
     * 當前Bean作用範圍
     */
    private String scope;

    public BeanDefinition() {
    }

    public BeanDefinition(Class clazz, String scope) {
        this.clazz = clazz;
        this.scope = scope;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
