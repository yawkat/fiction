/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package at.yawk.fiction.impl.fanfiction;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author yawkat
 */
@Getter
@RequiredArgsConstructor
public enum FfnCategory {
    ANIME("Anime/Manga", "anime"),
    BOOKS("Books", "book"),
    CARTOONS("Cartoons", "cartoon"),
    COMICS("Comics", "comic"),
    GAMES("Games", "game"),
    MISC("Misc", "misc"),
    MOVIES("Movies", "movie"),
    PLAYS("Plays/Musicals", "play"),
    TV_SHOWS("TV Shows", "tv");

    private final String name;
    private final String id;
}
