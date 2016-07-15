/*
 *  See the file "LICENSE.TXT" for the full license governing this code.
 */

/*
 *  See the file "LICENSE.TXT" for the full license governing this code.
 */

package com.aol.one.patch;

import com.aol.one.patch.PathTokens;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Madhu Ramanna <madhu.ramanna@advertising.com> dt 2/1/16.
 */

public class PathTokensTest {

  private PathTokens pathTokens;

  @Before
  public void setUp() {
    pathTokens = new PathTokens();
  }

  @Test
  public void testGetFirstToken() throws PatchException {
    pathTokens.setPath("/a");
    assertThat(pathTokens.getFirstToken(), is("a"));
  }

  @Test
  public void testGetFirstToken2() throws PatchException {
    pathTokens.setPath("/d/e/f");
    assertThat(pathTokens.getFirstToken(), is("d"));
  }

  @Test
  public void testCreation() throws PatchException {
    String path = "/a/b/c";
    PathTokens pt = new PathTokens(path);
    assertThat(pt.getLastToken(), is("c"));
    assertThat(pt.getFirstToken(), is("a"));
    assertThat(pt.getPath(), is(path));
    assertThat(pt.getLastTokenIndex(), is(2));
    assertThat(pt.getPathSansFirstToken(), is("/b/c"));
  }

  @Test
  public void testGetPathSansFirstToken() throws PatchException {
    PathTokens pt = new PathTokens("/foo/bar/zoo");
    assertThat(pt.getPathSansFirstToken(), is("/bar/zoo"));
  }

  @Test
  public void testGetPathSansFirstToken2() throws PatchException {
    PathTokens pt = new PathTokens("/foo");
    assertThat(pt.getPathSansFirstToken(), is("/"));
  }

}
