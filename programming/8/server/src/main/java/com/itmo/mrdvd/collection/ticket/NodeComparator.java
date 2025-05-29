package com.itmo.mrdvd.collection.ticket;

import com.itmo.mrdvd.Node;
import com.itmo.mrdvd.Ticket;
import java.util.Comparator;

public class NodeComparator implements Comparator<Node> {
  private final Comparator<Ticket> ticketComparator;

  public NodeComparator(Comparator<Ticket> ticketComparator) {
    this.ticketComparator = ticketComparator;
  }

  @Override
  public int compare(Node n1, Node n2) {
    return this.ticketComparator.compare(n1.getItem().getTicket(), n2.getItem().getTicket());
  }
}
