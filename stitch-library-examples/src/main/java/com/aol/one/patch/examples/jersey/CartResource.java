// Copyright 2015 AOL Platforms.

package com.aol.one.patch.examples.jersey;

/**
 * @author Madhu Ramanna <madhu.ramanna@advertising.com> dt 2/5/16.
 */

import com.aol.one.patch.PatchOperation;
import com.aol.one.patch.PatchOperationList;
import com.aol.one.patch.Patcher;
import com.aol.one.patch.PatcherFactory;

import com.aol.one.patch.examples.jersey.objects.Cart;
import org.glassfish.jersey.examples.httppatch.PATCH;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("/cart")
public class CartResource {

  @PATCH
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("/{cartId}")
  @Produces("text/plain")
  public String doPatch(@PathParam("cartId")
                       final int cartId,
                       /* accept a list of operations */
                       PatchOperationList patchOperationList) throws IOException {

    StringBuilder sb = new StringBuilder();
    Patcher patcher = PatcherFactory.getDefaultPatcher();
    Cart cart = new Cart(cartId);


    sb.append("Cart before patch: ");
    sb.append(cart);
    sb.append("\n-------------------\n");

    try {
      for (PatchOperation patchOperation: patchOperationList){
        patcher.patch(cart, patchOperation);
        sb.append("\nCart after patch " + patchOperation + " : ");
        sb.append(cart);
        sb.append("\n-------------------\n");
      }
    } catch (Exception ex) {
      sb.append("Exception occurred during patch: " + ex.toString());
    }

    sb.append("\n");

    return sb.toString();
  }

}
