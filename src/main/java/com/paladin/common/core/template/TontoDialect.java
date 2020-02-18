package com.paladin.common.core.template;

import java.util.HashSet;
import java.util.Set;

import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.standard.StandardDialect;
import org.thymeleaf.standard.processor.StandardXmlNsTagProcessor;
import org.thymeleaf.templatemode.TemplateMode;

public class TontoDialect extends AbstractProcessorDialect{
	 
	private static final String DIALECT_NAME = "Tonto Dialect";//定义方言名称
	
    public TontoDialect() {
        // 我们将设置此方言与“方言处理器”优先级相同
        // 标准方言, 以便处理器执行交错。
        super(DIALECT_NAME, "tt", StandardDialect.PROCESSOR_PRECEDENCE);
    }
    

    public Set<IProcessor> getProcessors(final String dialectPrefix) {
        Set<IProcessor> processors = new HashSet<IProcessor>();
        processors.add(new ConstantTagProcessor(dialectPrefix));//添加我们定义的标签
        processors.add(new StandardXmlNsTagProcessor(TemplateMode.HTML, dialectPrefix));
        return processors;
    }
}
