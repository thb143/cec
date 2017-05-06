<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="@* | node()">
		<xsl:copy>
			<xsl:apply-templates select="@* | node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="/SellTicketResult">
	  <SellTicketResult>	
	      <ResultCode><xsl:value-of select="ResultCode"/></ResultCode>
			<Order>
			   <cinemaOrderCode><xsl:value-of select="OrderNo" /></cinemaOrderCode>
			   <voucher>
			   		<verifyCode><xsl:value-of select="ValidCode" /></verifyCode>
			   		<printCode><xsl:value-of select="OrderNo" /></printCode>
			   	</voucher>
			   	<orderItems>
					<orderItem>
						<ticketCode><xsl:value-of select="OrderNo" /></ticketCode>
					</orderItem>
				</orderItems>
			</Order>
		</SellTicketResult>
	</xsl:template>
</xsl:stylesheet>