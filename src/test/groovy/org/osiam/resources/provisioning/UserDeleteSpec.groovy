/**
 * The MIT License (MIT)
 *
 * Copyright (C) 2013-2016 tarent solutions GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.osiam.resources.provisioning

import org.osiam.resources.converter.UserConverter
import org.osiam.resources.exception.ResourceNotFoundException
import org.osiam.storage.dao.UserDao
import org.springframework.security.authentication.encoding.PasswordEncoder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import spock.lang.Specification

import javax.persistence.NoResultException

class UserDeleteSpec extends Specification {

    def bCryptPasswordEncoder = Mock(BCryptPasswordEncoder)
    def userDao = Mock(UserDao)
    def userConverter = Mock(UserConverter)

    SCIMUserProvisioning scimUserProvisioning = new SCIMUserProvisioning(userConverter, userDao, bCryptPasswordEncoder, null)

    def uuidAsString = UUID.randomUUID().toString()

    def 'deleting an unknown user raises exception'() {

        when:
        scimUserProvisioning.delete(uuidAsString)

        then:
        1 * userDao.delete(uuidAsString) >> { throw new ResourceNotFoundException("User with id '${uuidAsString}' not found") }
        def rnfe = thrown(ResourceNotFoundException)

        rnfe.getMessage().with {
            contains('User')
            contains(uuidAsString)
        }
    }

    def 'deleting a user calls userDao.delete()'() {

        when:
        scimUserProvisioning.delete(uuidAsString)

        then:
        1 * userDao.delete(uuidAsString)
    }
}
