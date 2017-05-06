<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="@* | node()">
		<xsl:copy>
			<xsl:apply-templates select="@* | node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="/data">
	<data>
		<result><xsl:value-of select="@result" /></result>
		<message><xsl:value-of select="@message" /></message>
		<Order>
			<voucher>
				<verifyCode><xsl:value-of select="confirmationId" /></verifyCode>
				<printCode><xsl:value-of select="bookingId"/></printCode>
			</voucher>
			<cinemaOrderCode><xsl:value-of select="bookingId"/></cinemaOrderCode>
			<orderItems>
				<xsl:for-each select="seats/seat">
					<orderItem>
						<ticketCode>
							<xsl:value-of select="@ticketNo" />
						</ticketCode>
						<seatCode>
							<xsl:value-of select="@rowId" />_<xsl:value-of select="@columnId" />
						</seatCode>
					</orderItem>
				</xsl:for-each>
			</orderItems>
		</Order>
	</data>
	</xsl:template>
</xsl:stylesheet>