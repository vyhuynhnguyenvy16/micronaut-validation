/*
 * Copyright 2017-2026 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.validation

import io.micronaut.core.annotation.Introspected
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.micronaut.validation.validator.Validator
import jakarta.inject.Inject
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@MicronautTest
class GetterCascadeTest {

    @Inject
    lateinit var validator: Validator

    @Test
    fun testGetterValidCascadesIntoListElements() {
        assertEquals(listOf("items[0].name"), violationPaths(ListHolder(listOf(Item("")))))
    }

    @Test
    fun testGetterValidCascadesIntoMapValues() {
        assertEquals(listOf("items[key].name"), violationPaths(MapHolder(mapOf("key" to Item("")))))
    }

    @Test
    fun testValidOnFunctionCascadesIntoListElements() {
        assertEquals(listOf("items[0].name"), violationPaths(FunctionHolder()))
    }

    private fun violationPaths(bean: Any) = validator.validate(bean).map { it.propertyPath.toString() }

    @Introspected
    data class Item(@field:NotBlank val name: String)

    @Introspected
    data class ListHolder(@get:Valid val items: List<Item>)

    @Introspected
    data class MapHolder(@get:Valid val items: Map<String, Item>)

    @Introspected
    class FunctionHolder {
        @Valid
        fun getItems(): List<Item> = listOf(Item(""))
    }
}
