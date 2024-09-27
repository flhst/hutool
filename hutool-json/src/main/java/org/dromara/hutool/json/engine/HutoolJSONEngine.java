/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.json.engine;

import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.json.JSON;
import org.dromara.hutool.json.JSONConfig;
import org.dromara.hutool.json.JSONFactory;

import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;

/**
 * Hutool自身实现的JSON引擎
 *
 * @author Looly
 * @since 6.0.0
 */
public class HutoolJSONEngine extends AbstractJSONEngine {

	private JSONFactory jsonFactory;

	@Override
	public void serialize(final Object bean, final Writer writer) {
		initEngine();
		final JSON json = jsonFactory.parse(bean);
		json.write(jsonFactory.ofWriter(writer,
			ObjUtil.defaultIfNull(this.config, JSONEngineConfig::isPrettyPrint, false)));
	}

	@Override
	public <T> T deserialize(final Reader reader, final Object type) {
		initEngine();
		final JSON json = jsonFactory.parse(reader);
		return json.toBean((Type) type);
	}

	@Override
	protected void reset() {
		jsonFactory = null;
	}

	@Override
	protected void initEngine() {
		if(null != jsonFactory){
			return;
		}

		// 自定义配置
		final JSONConfig hutoolSJONConfig = JSONConfig.of();
		if(null != this.config){
			hutoolSJONConfig.setDateFormat(this.config.getDateFormat());
			hutoolSJONConfig.setIgnoreNullValue(this.config.isIgnoreNullValue());
		}

		this.jsonFactory = JSONFactory.of(hutoolSJONConfig, null);
	}
}
