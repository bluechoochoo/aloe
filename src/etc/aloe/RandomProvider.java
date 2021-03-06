/*
 * This file is part of ALOE.
 *
 * ALOE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * ALOE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with ALOE.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (c) 2012 SCCL, University of Washington (http://depts.washington.edu/sccl)
 */
package etc.aloe;

import java.util.Random;

/**
 * Global singleton Random class.
 *
 * @author Michael Brooks <mjbrooks@uw.edu>
 */
public abstract class RandomProvider {

    private static Random _instance = new Random();

    public static void setRandom(Random random) {
        _instance = random;
    }

    public static Random getRandom() {
        return _instance;
    }
}
