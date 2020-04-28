/*
 * Copyright 2020 Oliver Trevor and Suchin Ravi.
 *
 * This file is part of MarkSpace.
 *
 * MarkSpace is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MarkSpace is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MarkSpace.  If not, see <https://www.gnu.org/licenses/>.
 */

package security;

/**
 * Sets the parameters of the password hashing/salting used for security.
 * Do not change any of these values, except to increase them over time as computer processing power makes hashes easier to break.
 * Note that changing any of these values invalidates all existing passwords/salts in the database.
 * @see security.SecurityParameters
 */

/**
 * Sets the number of hash iterations and hash/salt sizes of the password hashing/salting algorithm.
 * Do not change any of these values, except to increase them over time as computer processing power makes hashes easier to break.
 * Note that changing any of these values invalidates all existing passwords/salts in the database.
 */
class SecurityParameters {
    /**
     * Specifies the number of iterations that the password hashing algorithm should run for. May need to be increased as computers get faster at hash-cracking.
     */
    public static final int PASSWORD_HASH_ITERATION_COUNT = 50000;

    /**
     * Specifies the length in bytes of the password hashes that will be generated to securely store users' passwords.
     */
    public static final int PASSWORD_HASH_LENGTH_BYTES = 64; // 512 bits

    /**
     * Specifies the length in bytes of the password salts that will be generated to securely store users' passwords.
     */
    public static final int PASSWORD_SALT_LENGTH_BYTES = 64; // 512 bits
};