/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package at.yawk.fiction.impl.fanfiction

/**
 * @author yawkat
 */
data class FfnSubCategory(
        val category: FfnCategory? = null,
        val crossover: Boolean? = null,

        val name: String? = null,
        val id: Int? = null,
        val estimatedStoryCount: Int? = null,
        val characters: Collection<FfnCharacter>? = null,
        val genres: Collection<FfnGenre>? = null,
        val languages: Collection<FfnLanguage>? = null,
        val worlds: Collection<FfnWorld>? = null
)