--
--      SpongeRemoteChests - Access your containers remotely.
--
--         The MIT License (MIT)
--
--      Copyright (c) 2017 Sandstone <https://github.com/ProjectSandstone/SpongeRemoteChests/>
--      Copyright (c) contributors
--
--
--      Permission is hereby granted, free of charge, to any person obtaining a copy
--      of this software and associated documentation files (the "Software"), to deal
--      in the Software without restriction, including without limitation the rights
--      to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
--      copies of the Software, and to permit persons to whom the Software is
--      furnished to do so, subject to the following conditions:
--
--      The above copyright notice and this permission notice shall be included in
--      all copies or substantial portions of the Software.
--
--      THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
--      IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
--      FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
--      AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
--      LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
--      OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
--      THE SOFTWARE.
--

CREATE TABLE IF NOT EXISTS `containers` (
  id INT NOT NULL AUTO_INCREMENT,
  owner CHAR(36) NOT NULL,
  name VARCHAR(255),
  world CHAR(36) NOT NULL,
  x INT NOT NULL,
  y INT NOT NULL,
  z INT NOT NULL,
  PRIMARY KEY (id)
)