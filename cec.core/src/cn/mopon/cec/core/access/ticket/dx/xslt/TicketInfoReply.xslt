<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="@* | node()">
		<xsl:copy>
			<xsl:apply-templates select="@* | node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="/root/data">
		<ticketOrder>
			<orderItems>
				<xsl:for-each select="ticketInfo/item">
					<ticketOrderItem>
						<ticketCode><xsl:value-of select="no" /></ticketCode>
						<seatRow><xsl:value-of select="row" /></seatRow>
						<seatCol><xsl:value-of select="column" /></seatCol>
					</ticketOrderItem>
				</xsl:for-each>
			</orderItems>	
		</ticketOrder>	
	</xsl:template>
</xsl:stylesheet>