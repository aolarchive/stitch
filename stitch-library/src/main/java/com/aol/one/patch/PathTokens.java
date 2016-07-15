/*
 *  See the file "LICENSE.TXT" for the full license governing this code.
 */

package com.aol.one.patch;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.StringTokenizer;

import static com.aol.one.patch.ErrorCodes.ERR_INVALID_PATH;
import static com.aol.one.patch.ErrorCodes.ERR_UNKNOWN;

/**
 * @author Madhu Ramanna &lt;madhu.ramanna@advertising.com&lt;
 */

class PathTokens extends ArrayList<String> {

  private static final char SLASH = '/';
  private static final String ROOT_PATH_TOKEN = "_ROOT_";
  private static final String SLASH_STR;

  private String path;

  public PathTokens() {
    // nothing to implement
  }

  public PathTokens(String path) throws PatchException {
    setPath(path);
  }


  public String getPath() {
    return path;
  }

  public void setPath(String path) throws PatchException {
    this.path = path;
    this.populate();
  }

  @Override
  public String get(int index) {
    if (index == -1) {
      return ROOT_PATH_TOKEN;
    } else {
      return super.get(index);
    }
  }

  public void populate() throws PatchException {

    if (StringUtils.isBlank(path)) {
      throw new PatchException(ERR_INVALID_PATH, path);
    }

    this.clear();


    if (!path.startsWith(SLASH_STR)) {
      throw new PatchRuntimeException(ERR_UNKNOWN,  "path does not start with: " + SLASH_STR);
    }

    StringTokenizer tokenizer = new StringTokenizer(path, SLASH_STR);

    while (tokenizer.hasMoreTokens()) {
      this.add(tokenizer.nextToken());
    }
  }


  public String getFirstToken() {
    if (this.isEmpty()) {
      return null;
    }
    return get(0);
  }

  public String getLastToken() {
    if (this.isEmpty()) {
      return null;
    }
    return get(size() - 1);
  }

  public int getLastTokenParentIndex() {
    return getLastTokenIndex() - 1;
  }

  public int getLastTokenIndex() {
    if (this.isEmpty()) {
      throw new PatchRuntimeException(ERR_UNKNOWN, "cannot get last token index when token list is empty");
    }
    return size() - 1;
  }

  public String getPathSansFirstToken() {
    if (this.size() < 1) {
      return null;
    }

    return SLASH + StringUtils.join(this.listIterator(1), SLASH);
  }

  static {
    char[] tmp = new char[1];
    tmp[0] = SLASH;
    SLASH_STR = new String(tmp);
  }

}
